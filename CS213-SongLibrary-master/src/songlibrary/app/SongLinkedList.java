package songlibrary.app;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import songlibrary.view.SongLibController;

public class SongLinkedList {
    // head node for easy access
    Node head;

    // constructor for list
    public SongLinkedList() {
        // try to go through the entire file and add all songs to the list
        try {
            Scanner sc = new Scanner(new File("./src/songlibrary/data/songList.txt"));

            // check if sc is empty
            if(!sc.hasNext()) {
                this.head = null;
                sc.close();
                return;
            }

            String name, artist, album, year;

            // initialize list head
            name = sc.nextLine();
            artist = sc.nextLine();
            album = sc.nextLine();
            year = sc.nextLine();
            if (album.compareTo("*EMPTY*") == 0) album = null;
            if (year.compareTo("*EMPTY*") == 0) year = null;
            Node head = new Node(name, artist, album, year);
            this.head = head;

            // populate the rest of the list
            Node tempNode = head;
            while(sc.hasNext()) {
                name = sc.nextLine();
                artist = sc.nextLine();
                album = sc.nextLine();
                year = sc.nextLine();
                if (album.compareTo("*EMPTY*") == 0) album = null;
                if (year.compareTo("*EMPTY*") == 0) year = null;
                tempNode.next = new Node(name, artist, album, year);

                tempNode = tempNode.next;
            }

            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error: Missing File");
        }
    }

    static class Node {
        // node variables
        String name;
        String artist;
        String album;
        String year;
        Node next;

        // constructor for all inputs
        Node(String name, String artist, String album, String year) {
            this.name = name;
            this.artist = artist;
            this.album = album;
            this.year = year;
            next = null;
        }
    }

    // prints the song list
    public void printList(SongLinkedList list) {
        Node currentNode = list.head;

        // traverse list and print info for each node
        while (currentNode != null) {
            System.out.print("Song Name: " + currentNode.name + ", Artist: " + currentNode.artist);
            if (currentNode.album != null) System.out.print(", Album: " + currentNode.album);
            if (currentNode.year != null) System.out.print(", Year: " + currentNode.year);
            System.out.println();

            currentNode = currentNode.next;
        }
    }

    // adds song to list
    public SongLinkedList addSong(SongLinkedList list, String name, String artist, String album, String year) {
        Node newNode = new Node(name, artist, album, year);
        
        // check if list is empty
        if (list.head == null) list.head = newNode;
        // insert node in alphabetical order
        else {
            Node tempNode = list.head;

            // make sure head node is before new node
            int comparedResult = tempNode.name.toLowerCase().compareTo(newNode.name.toLowerCase());
            if (comparedResult == 0) {
                comparedResult = tempNode.artist.toLowerCase().compareTo(newNode.artist.toLowerCase());
                // check to see if songs are identical
                if (comparedResult == 0) {
                    SongLibController.sendAlert("Error Adding Song", "Duplicate Song:", (newNode.name + " : " + newNode.artist));
                    return list;
                }
            }
            if (comparedResult > 0) {
                newNode.next = tempNode;
                list.head = newNode;
            }
            // traverse list to insert node alphabetically
            else {
                while (tempNode.next != null) {
                    // compare song name then compares artist name if song is identical
                    comparedResult = tempNode.next.name.toLowerCase().compareTo(newNode.name.toLowerCase());
                    if (comparedResult == 0) {
                        comparedResult = tempNode.next.artist.toLowerCase().compareTo(newNode.artist.toLowerCase());
                        // check to see if songs are identical
                        if (comparedResult == 0) {
                            SongLibController.sendAlert("Error Adding Song", "Duplicate Song:", (newNode.name + " : " + newNode.artist));
                            return list;
                        }
                    }

                    // takes result of comparedResult to find order. breaks loop if tempNode is after newNode
                    if (comparedResult > 0) {
                        newNode.next = tempNode.next;
                        break;
                    }
                    tempNode = tempNode.next;
                }
                tempNode.next = newNode;
            }
        }

        return list;
    }

    // save all songs onto a text file
    public void saveSongs(SongLinkedList list) {
        Node currentNode = list.head;

        // save all songs' info into a string
        String allSongs = "";
        while(currentNode != null) {
            allSongs = allSongs + currentNode.name + "\n" + currentNode.artist + "\n";
            // check if album is null
            if(currentNode.album == null) allSongs = allSongs + "*EMPTY*" + "\n";
            else allSongs = allSongs + currentNode.album + "\n";
            // check if year is null
            if (currentNode.year == null) allSongs = allSongs + "*EMPTY*" + "\n";
            else allSongs = allSongs + currentNode.year + "\n";

            currentNode = currentNode.next;
        }

        // try to save all songs onto songList.txt
        try {
            FileWriter fw = new FileWriter("./src/songlibrary/data/songList.txt");
            fw.write(allSongs);
            fw.close();
        } catch (IOException e) {
            System.out.println("Error: Missing File");
        }
    }

    // return ArrayList of song name and artists
    public ArrayList<String> returnSongNAList(SongLinkedList list) {
        Node currentNode = list.head;
        ArrayList<String> allSongs = new ArrayList<String>();

        // traverse list and add all song name and artists to arraylist
        while(currentNode != null) {
            allSongs.add(currentNode.name + " : " + currentNode.artist);

            currentNode = currentNode.next;
        }

        return allSongs;
    }

    // returns all details of a song using its name and artist
    public String returnSongDetails(SongLinkedList list, String name, String artist) {
        Node currentNode = list.head;
        
        // traverse list until you find a song that matches the name/artist description
        while(currentNode != null) {
            if (currentNode.name.toLowerCase().compareTo(name.toLowerCase()) == 0 && currentNode.artist.toLowerCase().compareTo(artist.toLowerCase()) == 0) {
                String songDetails = "Song Name: " + currentNode.name + "\nArtist: " + currentNode.artist + "\nAlbum: " + currentNode.album + "\nYear: " + currentNode.year;
                return songDetails;
            }
            currentNode = currentNode.next;
        }

        return "No Song Selected";
    }
}