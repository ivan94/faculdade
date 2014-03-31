#ifndef FILEMANAGER_H
#define FILEMANAGER_H
#include <string>

using namespace std;

class FileNotExistsException : exception{};

/*Estrutura intermediaria entre lógica e persistencia
  A tabela da planillha é conhecida da persistencia apenas como uma matriz de string,
  sendo essa string o valor da celula ou sua formula (celulas com formulas seus valores são dados pela formula)*/
struct FileContent{
    string* matrix;
    int rowSize, colSize;
};

class FileManager
{
    /*Nome e local do arquivo que esta sendo usado*/
    string fileName;
public:

    FileManager();
    //Carrega o conteúdo do arquivo e passa como FileContent
    FileContent openFile(string filename);

    //Salva no arquivo que está sendo usado
    void saveFile(FileContent content);
    //Salva em um novo arquivo
    void saveFileAs(string fileName, FileContent content);
};

#endif // FILEMANAGER_H
