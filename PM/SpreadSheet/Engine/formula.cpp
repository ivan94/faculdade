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

FormType Formula::processStringElement(string &e){

    char* tail;

    strtod(e.substr(1).c_str(), &tail);

    if(e.substr(0, 2).compare("AVG") == 0){ //Função de média
        return AVG;
    }else if(e.substr(0, 2).compare("SUM") == 0){ //Função de SOMA
        return SUM;
    }else if(('A' <= e[0] <= 'Z') || *tail='\0'){
        return CELL;
    }else if(e.compare("+") == 0){
        return PLUS;
    }else if(e.compare("-") == 0){
        return MINUS;
    }else if(e.compare("*") == 0){
        return MUL;
    }else if(e.compare("/") == 0){
        return DIV;
    }else{
        return NUM;
    }
}



void Formula::execute(){
    //TODO: formula logic
    stringstream strm((*(this->formula)).substr(1));

    string e;

    bool first = true;


    while(!strm.eof()){
        strm >> e;
        switch(this->processStringElement(e)){
            case AVG:
                break;
        case SUM:
        }
    }

}

Formula::~Formula(){
    if(this->formula != NULL) delete this->formula;
}
