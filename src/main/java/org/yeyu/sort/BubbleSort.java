package org.yeyu.sort;

class BubbleSort{
    public static void main(String[] args){
        int a[] = {49,38,65,97,76,13,27,49,78,34,12,64,5,4,62,99,98,54,56,17,18,23,34,15,35,25,53,51};
        bubbleSort(a);
        for(int i: a){
            System.out.println(i);
        }
    }

    static void bubbleSort(int[] a){
        for(int i=0; i<a.length-1; i++) {
            for(int j=0; j<a.length-1-i; j++) {
                if(a[j] > a[j+1]) {
                    switchit(a, j, j+1);
                }
            }
        }
    }

    static void switchit(int[] a, int i, int j) {
        int tmp;
        tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }
}