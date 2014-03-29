#include "tablecell.h"
#include <cstdlib>

TableCell::TableCell()
{
    this->value = "";
}

void TableCell::setFormula(string formula){
    if(this->getString()!=formula){
        this->formula.setFormula(formula);
        this->notifyDependeces();
    }
}

string TableCell::getString(){
    return this->formula.getValor();
}

double TableCell::getDouble(){
    return this->formula.getResult();
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

