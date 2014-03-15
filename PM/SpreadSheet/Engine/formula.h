#ifndef FORMULA_H
#define FORMULA_H
#include <string>

using namespace std;

class Formula
{
private:
    string* formula;
public:
    Formula();
    virtual ~Formula();
    void setFormula(string& formula);
    string getFormula();
    void execute();
};

#endif // FORMULA_H
