#include "tablecell.h"
#include <cstdlib>

TableCell::TableCell()
{
    this->value = "";
    this->formula = NULL;
}

void TableCell::setValue(string str){
    this->value = str;

    this->notifyDependences();
}

void TableCell::setFormula(string formula){
    this->formula = formula;
}

void TableCell::execFormula(){
    //TODO: Implementar essa bagaça
}

string TableCell::getString(){
    return this->value;
}

double TableCell::getDouble(){
    char* tailStr;
    double v = strtod(this->value, &tailStr);

    if(*tailStr != '\0'){
        exception e;
        throw e;
    }

    return v;
}

string TableCell::getFormula(){
    return this->formula;
}

void TableCell::notifyDependeces(){
    for(list<TableCell*>::const_iterator iterator = this->dependences.begin(), end = this->dependences.end();
        iterator != end;
        ++iterator){
        (*iterator)->execFormula();
    }
}

void TableCell::registerDependence(TableCell *cell){
    this->dependences.insert(cell);
}

void TableCell::unregisterDependence(TableCell *cell){
    this->dependences.remove(cell);
}
