#ifndef TABLE_H
#define TABLE_H
#include "tablecell.h"

class Table
{
    static const int COLUMNSIZE = 20;
    static const int ROWSIZE = 20;

    TableCell cells[COLUMNSIZE][ROWSIZE];

    string backFile;

    static Table* instance = NULL;

    Table();

public:
    static Table* getInstance();
    static void killInstance();

    TableCell* getCell(int x, int y);

    void loadTableFromFile(string fileLocation);

    void saveTable();

    void saveTableOnFile(string fileLocation);
};


#endif // TABLE_H
