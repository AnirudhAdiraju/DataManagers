package com.example.stt;

public class Item {
        String name;
        String stock;
        public Item(String name, String stock) {
            this.name = name;
            this.stock = stock;
        }
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getStock() {
            return stock;
        }

        public void setStock(String stock) {
            this.stock = stock;
        }

    }
