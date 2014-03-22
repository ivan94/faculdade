#ifndef FILEMANAGER_H
#define FILEMANAGER_H
#include <string>

using namespace std;


struct FileContent{
    string* matrix;
    int rowSize, colSize;
};

class FileManager
{
    string fileName;
public:

    FileManager();
    FileContent openFile(string filename);
    void saveFile(FileContent content);
    void saveFileAs(string fileName, FileContent content);
};

#endif // FILEMANAGER_H
