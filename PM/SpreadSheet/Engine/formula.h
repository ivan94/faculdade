#ifndef FORMULA_H
#define FORMULA_H
#include <string>
#include <stack>
#include <list>

using namespace std;
/*Referencia a tablecell*/
class TableCell;

enum ParseState{INIT, CELL, NUM, FUNC, OP};

/*Classe de exceção. Usado quando a formula está mal formulada*/
class FormulaParseException : public exception{
};

class Formula
{
private:
    string* formula;

    //Referencia para a célula que contém a formula
    TableCell* boundCell;

    //Lista de quais celulas a formula depende
    list<TableCell*>* dependencies;

    //Buffer contendo a formula empilhada
    stack<string>* bufferStack;

    //Seta o buffer registrando as dependencias
    void populateBuffer();
    //Retira os items do buffer e remove o registro de dependencias
    void unpopulateBuffer();
    //Função básica de parse da formula
    //É recebido um elemento da formula, como uma celula uma função ou um número, e retorna ele processado como um número
    //Ex: se passado A1 ele retorna o valor de A1
    string parse(string s);
    //Registra a dependencia na célula e salva a celula na lista
    void addDependence(string s);
    //Percorre a lista e retira o registro de dependencia de todas as células
    void removeDependencies();

    ParseState processElementType(string s, char* buf, char* bufFuncArg);

public:
    Formula(TableCell* cell);
    TableCell* getBoundCell();
    virtual ~Formula();

    void setFormula(string& formula);

    string getFormula();

    //Executa a fórmula setando o resultado como valor na célula associada
    void execute();
};

#endif // FORMULA_H
