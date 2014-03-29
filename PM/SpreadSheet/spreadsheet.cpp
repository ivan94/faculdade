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
}

MainWindow::~MainWindow()
{
    delete ui;
}

//slots//////////////////////////////////////////

void MainWindow::on_tableWidget_cellDoubleClicked(int row, int column)
{
    //Exibe formula na celula
    QTableWidgetItem* twi;
    twi=ui->tableWidget->item(row,column);
    if(twi!=NULL)twi->setText(SpreadSheet->getCell(row,column)->getFormula().c_str());
}


void MainWindow::on_tableWidget_currentCellChanged(int currentRow, int currentColumn, int previousRow, int previousColumn)
{
    //Captura string digitada
    QTableWidgetItem* twi;
    twi=ui->tableWidget->item(previousRow,previousColumn);
    if(twi==NULL){
        ui->tableWidget->setItem(previousRow, previousColumn,new QTableWidgetItem(""));
    }else{
        if(twi->text()!=NULL)SpreadSheet->getCell(previousRow,previousColumn)->setFormula(twi->text().toStdString());
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
    //New();
}

void MainWindow::on_actionOpen_triggered()
{
    //Open();
}

void MainWindow::on_actionSave_triggered()
{
    //Save();
}

void MainWindow::on_actionSaveAs_triggered()
{
   //SaveAs();
}

//Menu Edit
void MainWindow::on_actionInsert_Row_triggered()
{
    ui->tableWidget->insertRow(MaxRow++);
}

void MainWindow::on_actionInsert_Column_triggered()
{
    ui->tableWidget->insertColumn(MaxColumn++);
}
