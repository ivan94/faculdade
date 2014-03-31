#include "tablecell.h"
#include <cstdlib>

TableCell::TableCell() : formula(this)
{
    this->value = "";
}

void TableCell::setCellId(int r, int c){
    this->rId = r;
    this->cId = c;
}

int TableCell::getCellRId(){
    return this->rId;
}

int TableCell::getCellCId(){
    return this->cId;
}

list<Formula*>  TableCell::setValue(string v){
    this->value = v;
    this->notifyDependeces();
    return this->dependences;
}

list<Formula*>  TableCell::setFormula(string formula){
    this->formula.setFormula(formula);
    return this->dependences;
}

string TableCell::getString(){
    return this->value;
}

double TableCell::getDouble(){
    char *saved_locale;
    saved_locale = setlocale(LC_NUMERIC, "C");
    char* tail;
    double v = strtod(this->value.c_str(),&tail);
    if(*tail!='\0'){
        setlocale(LC_NUMERIC, saved_locale);
        throw NotANumberCellException();
    }
    setlocale(LC_NUMERIC, saved_locale);
    return v;
}

string TableCell::getFormula(){
    return this->formula.getFormula();
}

void TableCell::notifyDependeces(){
    for(list<Formula*>::const_iterator iterator = this->dependences.begin(), end = this->dependences.end();
        iterator != end;
        ++iterator){
        (*iterator)->execute();
    }
}

void TableCell::registerDependence(Formula *form){
    this->dependences.push_front(form);
}

void TableCell::unregisterDependence(Formula *form){
    this->dependences.remove(form);
}

