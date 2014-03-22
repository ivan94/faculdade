#include "filemanager.h"
#include <fstream>

FileManager::FileManager()
{
    this->fileName = "";
}

FileContent FileManager::openFile(string filename){
    ifstream file;
    file.open(filename);

    if(file.fail()){
        throw new exception;
    }

    int rowSize, colSize;

    file >> rowSize;
    file >> colSize;

    FileContent content;
    content.matrix = new string[rowSize*colSize];
    content.rowSize = rowSize;
    content.colSize = colSize;

    for(int i = 0; i<(colSize*rowSize);i++){
        string buffer;
        getline(file, buffer, ',');
        content.matrix[i] = buffer;
    }

    this->fileName = filename;

    file.close();

    return content;

}

void FileManager::saveFile(FileContent content){
    //Garante que o arquivo exista para salva-lo
    ifstream test(this->fileName);
    if(test.fail()){
        throw new exception;
    }
    test.close();

    ofstream file(this->fileName);

    file << content.rowSize << endl;
    file << content.colSize << endl;

    for(int i = 0; i < content.rowSize; i++){
        if(i > 0 ) file << endl;
        for(int j=0; j< content.colSize; j++){
            if(j > 0) file << ',';
            file << content.matrix[i*content.colSize + j];
        }
    }

}

void FileManager::saveFileAs(string fileName, FileContent content){
    //Muda o arquivo que esta sendo trabalhado

    this->fileName = fileName;

    //Cria o arquivo no sistema
    ofstream creator(fileName);
    creator.close();

    this->saveFile(content);
}


