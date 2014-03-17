#ifndef FORMULA_H
#define FORMULA_H
#include <string>

using namespace std;

enum FormType {AVG, SUM, CELL, NUM, PLUS, MINUS, DIV, MUL};

class Formula
{
private:
    string* formula;

    FormType processStringElement(string& e);
public:
    Formula();
    virtual ~Formula();
    void setFormula(string& formula);
    string getFormula();
    void execute();
};

#endif // FORMULA_H
