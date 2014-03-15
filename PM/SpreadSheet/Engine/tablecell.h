#ifndef TABLECELL_H
#define TABLECELL_H
#include <string>
#include <list>
#include "formula.h"

using namespace std;

class TableCell
{
    string value;

    Formula formula;

    list<Formula*> dependences;

    void notifyDependeces();

public:
    TableCell();

    void setValue(string& value);

    void setFormula(string& formula);

    string& getString();

    double getDouble();

    string getFormula();

    void registerDependence (Formula* form);

    void unregisterDependence (Formula* form);

};

#endif // TABLECELL_H
