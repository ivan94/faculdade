#include "formula.h"
#include "table.h"
#include <stack>
#include <sstream>
#include <cstdlib>
#include <cmath>

Formula::Formula()
{
    this->formula = NULL;
    this->bufferStack = new stack<string>;
    this->dependencies = new list<TableCell*>;
}
Formula::~Formula(){
    if(this->formula != NULL) delete this->formula;
    delete this->bufferStack;
    delete this->dependencies;
}

string Formula::getFormula(){
    if(this->formula == NULL) return "";
    return *(this->formula);
}

string Formula::getValor(){
    return this->valor;
}
double Formula::getResult(){
    return this->result;
}

void Formula::setFormula(string &formula){
    this->unpopulateBuffer();
    if(this->formula != NULL) delete this->formula;
    this->formula = new string(formula);
    this->populateBuffer();

    this->execute();

}

enum ParseState{INIT, CELL, NUM, FUNC, OP};

ParseState processElementType(string s, char* buf, char* bufFuncArg){
    //contador do argumento de função
    int bfaCount = 0;

    ParseState state = INIT;

    //For de processamento do parse
    unsigned int i;
    if(s == "+" ||s == "-" ||s == "*" ||s == "/")
        return OP;
    for (i = 0; i<s.length(); i++){
        char c = s[i];


        if(c=='('){
            // '(' só são suportadas se for uma função, logo deve ser o quarto caractere.
            //O teste se o estado é CELL é porque se o que foi lido foi apenas número, não faz sentido tratar como função.
            //O estado chega a cell apenas se foi lido letra inicialmente
            if(i == 3 && state==CELL){
                state = FUNC;
            }else{
                throw new exception;
            }
        }else if(c==')'){
            //')' só é suportado se o termo for uma função e for o último elemento do termo
            if(state == FUNC && i==(s.length()-1)){
                break;
            }else{
                throw new exception;
            }
        }else if(c == ':'){
            //':' especifica uma range de celulas, que só é suportado em funções
            if(state != FUNC){
                throw new exception;
            }else{
                bufFuncArg[bfaCount] = c;
                bfaCount++;
            }
        }
        else if('A' <= c && c <= 'Z'){
            //Se começa com letra deve ser uma celula ou função.
            //É colocado inicialmente como celula
            if(state == INIT){
                state = CELL;
            //Não é suportado letras depois de números.
            }else if(state == NUM){
                throw new exception;
            //Se é uma função passa a ler no
            }else if(state == FUNC){
                bufFuncArg[bfaCount] = c;
                bfaCount++;
            }
            buf[i] = c;
        }
        else if(('0' <= c && c <= '9') || c == '.'){
            //Se começa com um número só pode ser um número
            if(state == INIT) state = NUM;
            //Coloca no bufer correto
            if(state == FUNC){
                bufFuncArg[bfaCount] = c;
                bfaCount++;
            }
            //'.' só é suportado em números
            if(state != NUM && c == '.'){
                throw new exception;
            }
            buf[i] = c;
        }
    }
    bufFuncArg[bfaCount] = '\0';
    return state;
}

void convertCell(string cell,int *row,int *col){
    int exp=*col=*row=0;

    for(int i=cell.size();i>=0;--i){
        if(cell[i]>='0' && cell[i]<='9'){
            *col += (cell[i]-'0')*pow(10,cell.size()-i-1);
        }else{
            if('0'<=cell[i+1] && cell[i+1]<='9')exp=i;

            *row += (cell[i]-'A'+1)*pow(26,exp-i);
        }
    }
}

void convertRange(string range, int* r1, int* c1, int* r2, int* c2){
    int row_1,col_1,row_2,col_2;

    int sepPos = range.find(':'); //posição do separador ':'
    string cell_1 = range.substr(0, sepPos - 1);
    string cell_2 = range.substr(sepPos + 1);

    convertCell(cell_1,&row_1,&col_1);
    convertCell(cell_2,&row_2,&col_2);

    *r1 = min(row_1, row_2);
    *r2 = max(row_1, row_2);

    *c1 = min(col_1, col_2);
    *c2 = max(col_1, col_2);
}

void Formula::addDependence(string s){
    char buf[s.length()];
    char bufAux[s.length()];

    TableCell* tc;
    int r, c;
    int r1, c1, r2, c2;

    ParseState state = processElementType(s, buf, bufAux);
    switch(state){
        case CELL:
            convertCell(s, &r, &c);
            tc = Table::getInstance()->getCell(r,c);
            tc->registerDependence(this);
            this->dependencies->push_front(tc);
            break;
        case FUNC:
            convertRange(s, &r1, &c1, &r2, &c2);
            for(r=r1; r<=r2; r++){
                for(c=c1; c<=c2; c++){
                    tc = Table::getInstance()->getCell(r,c);
                    tc->registerDependence(this);
                    this->dependencies->push_front(tc);
                }
            }
            break;
        default:
            break;
    }
}

void Formula::removeDependencies(){
    while(!this->dependencies->empty()){
        TableCell* tc = this->dependencies->front();
        tc->unregisterDependence(this);
        this->dependencies->pop_front();
    }
}

//Remove elementos do buffer e tira as dependencias
void Formula::unpopulateBuffer(){
    while (!this->bufferStack->empty()) {
        string s = this->bufferStack->top();
        //tira o registro da dependencia
        this->bufferStack->pop();
    }
    this->removeDependencies();
}

//insere os elementos no buffer e registra as dependencias
void Formula::populateBuffer(){
    stringstream ss;
    ss<<(this->formula->substr(1));

    while(!ss.eof()){
        string s;
        ss >> s;
        this->addDependence(s);
        this->bufferStack->push(s);

    }
}

double sum(string range){
    int sum=0.0;
    int row_1,col_1,row_2,col_2;

    convertRange(range, &row_1, &col_1, &row_2, &col_2);

    for(int i=row_1;i<=row_2;i++){
        for(int j=col_1;j<=col_2;j++){
            sum+=Table::getInstance()->getCell(i,j)->getDouble();
        }
    }
    return sum;
}

double avg(string range){
    int sum=0.0;
    int row_1,col_1,row_2,col_2;

    convertRange(range, &row_1, &col_1, &row_2, &col_2);

    double d = 0;
    for(int i=row_1;i<=row_2;i++){
        for(int j=col_1;j<=col_2;j++){
            sum+=Table::getInstance()->getCell(i,j)->getDouble();
            d++;
        }
    }
    return sum/d;
}

string Formula::parse(string s){
    //Buffer do termo lido e do argumento de função
    char buf[s.length()];
    char bufFuncArg[s.length()];

    ParseState state = processElementType(s, buf, bufFuncArg);

    string r;
    stringstream rs;


    if(state == CELL){
        int r, c;
        convertCell(s, &r, &c);
        rs << (Table::getInstance()->getCell(r,c)->getDouble());
    }else if(state == FUNC){
        buf[3] = '\0';
        string s = "SUM";
        string a = "AVG";
        if(s == buf){
            rs << (sum(string(bufFuncArg)));
        }else if(a == buf){
            rs << (avg(string(bufFuncArg)));
        }else{
            throw new exception;
        }
    }else{
        return s;
    }
    rs >> r;
    return r;
}

void Formula::execute(){
    stack<string> execStack;
    stack<string> bsCopy(*(this->bufferStack)); //cópia do bufferStack que pode ser modificada
    while(!bsCopy.empty()){
        execStack.push(this->parse(bsCopy.top()));
        bsCopy.pop();
    }

    string op1, op2;
    double result;
    char* tailStr;

    op1=execStack.top();
    execStack.pop();
    result=strtod(op1.c_str(), &tailStr);

    while(!execStack.empty()){
        if(execStack.top() == "+"){
            result+=strtod(op2.c_str(), &tailStr);

        }else if(execStack.top() == "-"){
            result-=strtod(op2.c_str(), &tailStr);

        }else if(execStack.top() == "*"){
            result*=strtod(op2.c_str(), &tailStr);

        }else if(execStack.top() == "/"){
            result/=strtod(op2.c_str(), &tailStr);

        }else{
            op1 = op2;
            op2 = execStack.top();
        }
        execStack.pop();
    }
    this->result=result;

    stringstream aux;
    aux<<result;
    string valor;
    aux>>valor;
    this->valor= valor;
}
