#-------------------------------------------------
#
# Project created by QtCreator 2014-03-07T10:59:36
#
#-------------------------------------------------

QT       += core gui

greaterThan(QT_MAJOR_VERSION, 4): QT += widgets

TARGET = SpreadSheet
TEMPLATE = app


SOURCES += main.cpp\
    Engine/tablecell.cpp \
    Engine/table.cpp \
    Persistence/filemanager.cpp \
    Engine/formula.cpp \
    spreadsheet.cpp

HEADERS  += \
    Engine/tablecell.h \
    Engine/table.h \
    Persistence/filemanager.h \
    Engine/formula.h \
    spreadsheet.h

FORMS    += mainwindow.ui
