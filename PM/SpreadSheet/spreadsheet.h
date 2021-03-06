#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include <QMainWindow>
#include <QtWidgets>
#include "Engine/table.h"

namespace Ui {
class MainWindow;
}

class MainWindow : public QMainWindow
{

    bool docSaved;

    Q_OBJECT

    void updateInterface();

public:
    explicit MainWindow(QWidget *parent = 0);
    ~MainWindow();

private slots:
    //Eventos observados
    void on_tableWidget_cellDoubleClicked(int row, int column);
    void on_tableWidget_currentCellChanged(int currentRow, int currentColumn, int previousRow, int previousColumn);

    //Ações do Menu
    void on_actionNew_triggered();
    void on_actionOpen_triggered();
    void on_actionSave_triggered();
    void on_actionSaveAs_triggered();

    void on_actionSpreadSheet_triggered();

protected:

private:
    Ui::MainWindow *ui;

    //
    int MaxRow;
    int MaxColumn;
    Table* SpreadSheet;
};

#endif // MAINWINDOW_H
