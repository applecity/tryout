package org.yeyu.sort;

public class SimpleSelectionSort {
    public static void main(String[] args){
        int a[] = {49,38,65,97,76,13,27,49,78,34,12,64,5,4,62,99,98,54,56,17,18,23,34,15,35,25,53,51};
        simpleSelectionSort(a);
        for(int i: a){
            System.out.println(i);
        }
    }

    static void simpleSelectionSort(int[] a){
        for(int i=0; i<a.length-1; i++) {
            int tmp = a[i];
            int pos = i;
            for(int j=i+1; j<a.length; j++) {
                if(a[j]<tmp) {
                    tmp = a[j];
                    pos = j;
                }
            }
            a[pos]=a[i];
            a[i] = tmp;
        }
    }

    static void switchit(int[] a, int i, int j) {
        int tmp;
        tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }
}
