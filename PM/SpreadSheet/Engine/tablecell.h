#ifndef TABLECELL_H
#define TABLECELL_H
#include <string>
#include <list>
#include "formula.h"

using namespace std;

class Formula;

class NotANumberCellException : exception{
};

class TableCell
{
    string value;

    Formula formula;

    list<Formula*> dependences;

    void notifyDependeces();

    int rId, cId;

public:

    void setCellId(int r, int c);

    int getCellRId();

    int getCellCId();

    TableCell();

    list<Formula*> setValue(string v);

    list<Formula*>  setFormula(string formula);

    string getString();

    double getDouble();

    string getFormula();

    void registerDependence (Formula* form);

    void unregisterDependence (Formula* form);

};

#endif // TABLECELL_H
