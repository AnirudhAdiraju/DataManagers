package com.example.stt;

public class Item {
        String name;
        int stock;
        public Item(String name, int stock) {
            this.name = name;
            this.stock = stock;
        }
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getStock() {
            return stock;
        }

        public void setStock(int stock) {
            this.stock = stock;
        }

    }
