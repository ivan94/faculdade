#include "spreadsheet.h"
#include <QApplication>

int main(int argc, char *argv[])
{
    QApplication a(argc, argv);
    MainWindow w;
    w.setWindowTitle("SpreadSheet");
    w.show();

    return a.exec();
}
