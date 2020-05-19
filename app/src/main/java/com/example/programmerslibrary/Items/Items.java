package com.example.programmerslibrary.Items;


import com.example.programmerslibrary.models.Book;
import com.example.programmerslibrary.models.Reader;

import java.util.ArrayList;
//контекст данных включает два списка читателей и книги
//несколько удобных методов для работы
//чтобы отобразить список всех нужен getList
//в теле метода создать новый ArrayList в котором будут и читатели и книги
//два метода добавить читателя и книгу
//все мотоды static
//метод save(сохранять отльеные два списка) и setList(будет перезгружен

public class Items {
    public static ArrayList<Reader> readers = new ArrayList<Reader>();
    public static ArrayList<Book> books = new ArrayList<Book>();

    public static void add(Reader person){
        readers.add(person);
    }
    public static void add(Book book){
        books.add(book);
    }

    public static void remove(Reader person){ readers.remove(person);}
    public static void remove(Book book){ books.remove(book);}

    public static void setListOfReaders(ArrayList<Reader> newList){
        readers = newList;
    }
    public static void setListOfBooks(ArrayList<Book> newList){
        books = newList;
    }

    public static ArrayList<Reader> getListofReaders(){
        ArrayList<Reader> list = new ArrayList<Reader>();
        list.addAll(readers);
        return list;
    }
    public static ArrayList<Book> getListofBooks(){
        ArrayList<Book> list = new ArrayList<Book>();
        list.addAll(books);
        return list;
    }

    public static Reader getStudent(int index){
        return readers.get(index);
    }
    public static Book getBook(int index){
        return books.get(index);
    }

    public static int indexOfReader(Reader reader){
        return readers.indexOf(reader);
    }
    public static int indexOfBook(Book book){
        return books.indexOf(book);
    }
}


