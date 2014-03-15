#include "tablecell.h"
#include <cstdlib>

TableCell::TableCell()
{
    this->value = "";
}

void TableCell::setValue(string& value){
    this->value = value;

    this->notifyDependeces();
}

void TableCell::setFormula(string& formula){
    this->formula.setFormula(formula);
}

string& TableCell::getString(){
    return this->value;
}

double TableCell::getDouble(){
    char* tailStr;
    double v = strtod(this->value.c_str(), &tailStr);

    //tailStr é o que sobra da string após a conversão. Se não sobrar uma string vazia o valor não é um numero
    if(*tailStr != '\0'){
        exception e; //TODO: create specific exception
        throw e;
    }

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
