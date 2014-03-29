#ifndef TABLE_H
#define TABLE_H
#include "tablecell.h"
#include "../Persistence/filemanager.h"
/*
 * Classe principal da lógica da planillha
 * Abstração da tabela em sí
 */
class Table
{
    //Tamanhos da planilha
    static const int COLUMNSIZE = 1000;
    static const int ROWSIZE = 1000;

    TableCell cells[COLUMNSIZE][ROWSIZE];

    //Referencia a classe de persistencia
    FileManager manager;

    //A instanciação da classe é controlada, só sendo permitida uma instancia por programa, usando o metodo statico getInstance()
    //A instancia quando criada é salva nesse ponteiro estático
    static Table* instance;

    //Construtor privado
    Table();

    //Gera o FileContent de acorodo com o conteúdo atual da planilha
    FileContent tableToFileContent();

public:
    //Metodos para se obter e remover a instancia da classe
    static Table* getInstance();
    static void killInstance();

    //Retorna uma referencia para a celula apontada
    TableCell* getCell(int x, int y);


    //Metodos de intermedio com a persistencia
    void loadTableFromFile(string fileLocation);

    void saveTable();

    void saveTableOnFile(string fileLocation);
};


#endif // TABLE_H
