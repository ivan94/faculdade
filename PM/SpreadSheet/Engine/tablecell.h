#ifndef TABLECELL_H
#define TABLECELL_H
#include <string>
#include <list>

using namespace std;

class TableCell
{
    string value;

    string formula;

    list<TableCell*> dependences;

    void notifyDependeces();

    void registerDependence (TableCell* cell);

    void unregisterDependence (TableCell* cell);

public:
    TableCell();

    void setValue(string* str);

    void setFormula(string formula);

    void execFormula();

    string getString();

    double getDouble();

    string getFormula();

};

#endif // TABLECELL_H
