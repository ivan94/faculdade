#ifndef TABLE_H
#define TABLE_H
#include "tablecell.h"

class Table
{
    static const int COLUMNSIZE = 20;
    static const int ROWSIZE = 20;

    TableCell* cells[COLUMNSIZE][ROWSIZE];

    static Table* instance = NULL;

    Table();

public:
    static Table* getInstance();
};

#endif // TABLE_H
