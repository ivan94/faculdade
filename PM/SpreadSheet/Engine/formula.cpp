#include "formula.h"
#include <stack>
#include <sstream>
#include <cstdlib>

Formula::Formula()
{
    this->formula = NULL;
}

void Formula::setFormula(string &formula){
    this->formula = new string(formula);
    //TODO: bind to dependence
}

string Formula::getFormula(){
    if(this->formula == NULL) return NULL; //Raise Exception
    return *(this->formula);
}

void Formula::execute(){
    //TODO: formula logic
    stringstream strm(*(this->formula));

    string op1, op2;

    double result, op;

    strm >> op1;

    while(strm.eof()){
        strm >> op2;
        strm >> operation;

        char* tail;
        double n;
        n = strtod(op2.c_str(), &tail);

        //Testa o operador 2
        if(*tailStr == '\0'){ //é um número
            op = n;
        }else if(op2.substr(0, 2).compare("AVG") == 0){ //Função de média
            //fazer logica
        }else if(op2.substr(0, 2).compare("SUM") == 0){ //Função de SOMA
            //fazer logica
        }else if(op2.compare("+") == 0){
            result += op;
        }else if(op2.compare("-") == 0){
            result -= op;
        }else if(op2.compare("*") == 0){
            result *= op;
        }else if(op2.compare("/") == 0){
            result /= op;
        }
    }


}

Formula::~Formula(){
    if(this->formula != NULL) delete this->formula;
}
