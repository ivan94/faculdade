#include "table.h"

Table* Table::instance = NULL;

Table::Table()
{
}

Table* Table::getInstance(){
    if(Table::instance == NULL)
        Table::instance = new Table();
    return Table::instance;
}
void Table::killInstance(){
    delete Table::instance;
    Table::instance = NULL;
}

TableCell* Table::getCell(int x, int y){
    if(x < 0 || x >= ROWSIZE || y < 0 || y >= COLUMNSIZE)
        throw new exception;

    return &(this->cells[x][y]);
}

FileContent Table::tableToFileContent(){
    FileContent content;
    content.matrix = new string[ROWSIZE*COLUMNSIZE];
    for(int i = 0; i<ROWSIZE; i++){
        for(int j=0; j<COLUMNSIZE; j++){
            string f = this->cells[i][j].getFormula();
            if(f==""){
                content.matrix[i*COLUMNSIZE + j] = this->cells[i][j].getString();
            }else{
                content.matrix[i*COLUMNSIZE + j] = f;
            }
        }
    }

    return content;
}

void Table::loadTableFromFile(string fileLocation){

    FileContent cont = this->manager.openFile(fileLocation);

    for(int i = 0; i<cont.rowSize && i<ROWSIZE; i++){
        for(int j = 0; j<cont.colSize && j<COLUMNSIZE; j++){
            //Verifica se o valor é uma formula, caracterizada por iniciar com o caractere '='
            if(cont.matrix[i*COLUMNSIZE + j][0] == '='){
                //this->cells[i][j].setValue("");
                this->cells[i][j].setFormula(cont.matrix[i*COLUMNSIZE + j]);
            }else{
                //this->cells[i][j].setValue(cont.matrix[i*COLUMNSIZE + j]);
                this->cells[i][j].setFormula(cont.matrix[i*COLUMNSIZE + j]);
            }
        }
    }
    delete[] cont.matrix;
}

void Table::saveTable(){
    FileContent content = this->tableToFileContent();
    this->manager.saveFile(content);
    delete[] content.matrix;
}

void Table::saveTableOnFile(string fileLocation){
    FileContent content = this->tableToFileContent();
    this->manager.saveFileAs(fileLocation, content);
    delete[] content.matrix;
}


