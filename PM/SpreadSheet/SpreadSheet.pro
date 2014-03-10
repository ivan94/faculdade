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
        mainwindow.cpp \
    Engine/tablecell.cpp \
    Engine/table.cpp

HEADERS  += mainwindow.h \
    Engine/tablecell.h \
    Engine/table.h

FORMS    += mainwindow.ui
