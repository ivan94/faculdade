#include "spreadsheet.h"
#include "ui_mainwindow.h"
#include <sstream>
#define n_init_row 1000
#define n_init_col 1000

//Função auxiliar para gerar os labels verticais(linhas)
void gerarLabels(QStringList& lista,int max){
    int aux=0;
    char label[5]={'A','\0','\0','\0','\0'};
    lista.insert(0,label);
    for(int i=1;i<max;i++){
        label[aux]++;
        for(int j=3;j>=0;j--){
            if(label[j]>'Z'){
                label[j]='A';
                if(j==0){
                    aux++;
                    label[aux]='A';
                }else{
                    label[j-1]++;
                }
            }
            label[4]='\0';
            lista.insert(i,label);
        }
    }
}

MainWindow::MainWindow(QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::MainWindow)
{
    ui->setupUi(this);

    //Iniciando parametros da tabela na interface
    MaxRow=n_init_row;
    MaxColumn=n_init_col;
    ui->tableWidget->setRowCount(n_init_row);
    ui->tableWidget->setColumnCount(n_init_col);

    //Adionar labels
    QStringList lista;
    gerarLabels(lista,n_init_row);
    ui->tableWidget->setVerticalHeaderLabels(lista);
    ui->tableWidget->verticalHeader()->adjustSize();

    //Associar tabela funcional a interface
    SpreadSheet= Table::getInstance();

    this->docSaved = true;
}

MainWindow::~MainWindow()
{
    delete ui;
}

void MainWindow::updateInterface(){
    for(int i = 0; i < Table::ROWSIZE; i++){
        for(int j = 0; j < Table::COLUMNSIZE; j++){
            QTableWidgetItem* twi=ui->tableWidget->item(i,j);
            if(twi!=NULL)
                twi->setText(SpreadSheet->getCell(i,j)->getString().c_str());
            else
                ui->tableWidget->setItem(i, j,new QTableWidgetItem(SpreadSheet->getCell(i,j)->getString().c_str()));
        }
    }
    this->update();
}

//slots//////////////////////////////////////////

void MainWindow::on_tableWidget_cellDoubleClicked(int row, int column)
{
    //Exibe formula na celula
    QTableWidgetItem* twi;
    twi=ui->tableWidget->item(row,column);
    if(SpreadSheet->getCell(row,column)->getFormula() == ""){
        if(twi!=NULL)twi->setText(SpreadSheet->getCell(row,column)->getString().c_str());
    }else{
        if(twi!=NULL)twi->setText(SpreadSheet->getCell(row,column)->getFormula().c_str());
    }
}


void MainWindow::on_tableWidget_currentCellChanged(int currentRow, int currentColumn, int previousRow, int previousColumn)
{

    //Captura string digitada
    QTableWidgetItem* twi;
    twi=ui->tableWidget->item(previousRow,previousColumn);
    if(twi==NULL){
        ui->tableWidget->setItem(previousRow, previousColumn,new QTableWidgetItem(""));
    }else{
        if(twi->text()!=NULL){
            string s = twi->text().toStdString();
            if(s != SpreadSheet->getCell(previousRow,previousColumn)->getString()){//Só muda se for texto novo
                try{
                    this->docSaved = false;
                    list<Formula*> l;
                    if(s[0] == '='){
                        l = SpreadSheet->getCell(previousRow,previousColumn)->setFormula(s);
                    }else{
                        SpreadSheet->getCell(previousRow,previousColumn)->setFormula("");
                        l = SpreadSheet->getCell(previousRow,previousColumn)->setValue(s);
                    }
                    for(list<Formula*>::const_iterator iterator = l.begin(), end = l.end();
                        iterator != end;
                        ++iterator){
                        int row = (*iterator)->getBoundCell()->getCellRId();
                        int col = (*iterator)->getBoundCell()->getCellCId();
                        ui->tableWidget->item(row,col)->setText((*iterator)->getBoundCell()->getString().c_str());
                    }
                    ui->tableWidget->item(previousRow,previousColumn)->setBackgroundColor(QColor(255,255,255));
                }catch(FormulaParseException &e){
                    ui->tableWidget->item(previousRow,previousColumn)->setBackgroundColor(QColor(255,100,100));

                }catch(NotANumberCellException &e){
                    ui->tableWidget->item(previousRow,previousColumn)->setBackgroundColor(QColor(255,100,100));
                }
            }
        }
    }

    //Exibe formula no TextEdit
    stringstream ss; string str;
    ss<<ui->tableWidget->verticalHeaderItem(currentRow)->text().toStdString() <<currentColumn+1;
    ss>>str;
    str+=" : "+SpreadSheet->getCell(currentRow,currentColumn)->getFormula();
    ui->textEdit->setText(str.c_str());

    //Exibe valor na celula anterior
    twi=ui->tableWidget->item(previousRow,previousColumn);
    if(twi!=NULL)twi->setText(SpreadSheet->getCell(previousRow,previousColumn)->getString().c_str());
}

//Menu File
void MainWindow::on_actionNew_triggered()
{
    if(!this->docSaved){
        //exibe dialogo de salvar
        QMessageBox mb;
        mb.setWindowTitle("Salvar alterações");
        mb.setText("O documento foi modificado.");
        mb.setInformativeText("Deseja salvar as mudanças?");
        mb.setStandardButtons(mb.Save|mb.Discard|mb.Cancel);
        mb.setDefaultButton(mb.Save);

        int op = mb.exec();
        switch (op) {
        case QMessageBox::Save:
                this->on_actionSave_triggered();
            break;
        case QMessageBox::Discard:
            break;
        case QMessageBox::Cancel:
            return;
            break;
        default:
            break;
        }
    }
    Table::killInstance();
    SpreadSheet = Table::getInstance();
    this->updateInterface();

    this->docSaved = true;
}

void MainWindow::on_actionOpen_triggered()
{
    if(!this->docSaved){
        //exibe dialogo de salvar
        QMessageBox mb;
        mb.setWindowTitle("Salvar alterações");
        mb.setText("O documento foi modificado.");
        mb.setInformativeText("Deseja salvar as mudanças?");
        mb.setStandardButtons(mb.Save|mb.Discard|mb.Cancel);
        mb.setDefaultButton(mb.Save);

        int op = mb.exec();
        switch (op) {
        case QMessageBox::Save:
                this->on_actionSave_triggered();
            break;
        case QMessageBox::Discard:
            break;
        case QMessageBox::Cancel:
            return;
            break;
        default:
            break;
        }
    }

    QString fileName = QFileDialog::getOpenFileName(this);
    if(fileName.isNull())
        return;
    SpreadSheet->loadTableFromFile(fileName.toStdString());

    this->updateInterface();

}

void MainWindow::on_actionSave_triggered()
{
    try{
        SpreadSheet->saveTable();
    }catch(FileNotExistsException){
        QString fileName = QFileDialog::getSaveFileName(this);
        if(fileName.isNull())
            return;
        SpreadSheet->saveTableOnFile(fileName.toStdString());
    }
    this->docSaved = true;
}

void MainWindow::on_actionSaveAs_triggered()
{
    QString fileName = QFileDialog::getSaveFileName(this);
    if(fileName.isNull())
        return;
    SpreadSheet->saveTableOnFile(fileName.toStdString());
    this->docSaved = true;
}
void MainWindow::on_actionSpreadSheet_triggered()
{
    QMessageBox::about(this,tr("Sobre SpreadSheet"),tr("Neste trabalho foi implementado uma planilha eletrônica básica,"
   "capaz de armazenar números e strings em suas células e processar fórmulas simples em notação pós-fixada,"
   "com possibilidade de uso das funções de soma (SUM) e média (AVG). Feito por David Andrade e Ivan Fernandes."));
}
