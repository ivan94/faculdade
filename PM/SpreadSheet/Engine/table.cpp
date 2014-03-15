#include "table.h"

Table* Table::instance = NULL;

Table::Table()
{
    this->backFile = "";
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
    if(x < 0 || x >= ROWSIZE || y < 0 || y > COLUMNSIZE)
        throw new exception;

    return &(this->cells[x][y]);
}


