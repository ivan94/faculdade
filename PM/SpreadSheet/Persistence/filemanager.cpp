#include "filemanager.h"
#include <fstream>

FileManager::FileManager()
{
    this->fileName = "";
}

FileContent FileManager::openFile(string filename){
    ifstream file;
    file.open(filename.c_str());

    if(file.fail()){
        throw FileNotExistsException();
    }

    int rowSize, colSize;

    file >> rowSize;
    file >> colSize;

    FileContent content;
    content.matrix = new string[rowSize*colSize];
    content.rowSize = rowSize;
    content.colSize = colSize;

    for(int i = 0; i<rowSize;i++){
        for(int j=0; j<colSize;j++){
            string buffer;
            getline(file, buffer, ';');
            content.matrix[i*colSize+j] = buffer;
        }
    }
    this->fileName = filename;

    file.close();

    return content;

}

void FileManager::saveFile(FileContent content){
    //Garante que o arquivo exista para salva-lo
    ifstream test(this->fileName.c_str());
    if(test.fail()){
        throw FileNotExistsException();
    }
    test.close();

    ofstream file(this->fileName.c_str());

    file << content.rowSize << endl;
    file << content.colSize << endl;

    for(int i = 0; i < content.rowSize; i++){
        if(i > 0 ) file << ';';
        for(int j=0; j< content.colSize; j++){
            if(j > 0) file << ';';
            file << content.matrix[i*content.colSize + j];
        }
    }

}

void FileManager::saveFileAs(string fileName, FileContent content){
    //Muda o arquivo que esta sendo trabalhado

    this->fileName = fileName;

    //Cria o arquivo no sistema
    ofstream creator(fileName.c_str());
    creator.close();

    this->saveFile(content);
}

