#include "table.h"

Table::Table()
{
    this->backFile = "";
}

static Table* Table::getInstance(){
    if(Table::instance == NULL)
        Table::instance = new Table();
    return Table::instance;
}

static void Table::killInstance(){
    delete Table::instance;
    Table::instance = NULL;
}

TableCell* Table::getCell(int x, int y){
    if(x < 0 || x >= ROWSIZE || y < 0 || y > COLUMNSIZE)
        throw new exception;

    return &(this->cells[x][y]);
}


