#ifndef FORMULA_H
#define FORMULA_H
#include <string>
#include <stack>
#include <list>

using namespace std;

class TableCell;

class Formula
{
private:
    string* formula;
    string valor;
    double result;

    list<TableCell*>* dependencies;

    stack<string>* bufferStack;
    void populateBuffer();
    void unpopulateBuffer();
    string parse(string s);
    void addDependence(string s);
    void removeDependencies();

public:
    Formula();
    virtual ~Formula();
    void setFormula(string& formula);
    string getFormula();
    string getValor();
    void execute();
    double getResult();
};

#endif // FORMULA_H
