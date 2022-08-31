package com.qooapp.opensdk.sample.qooapp.model;

import java.util.List;

/**
 * Created by heliang
 */
public class PageProductResponse {
    private int code;
    private String message;

    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public class PagerBean {
        private int page;
        private int size;
        private int total;

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }
    }

    public class DataBean {
        private PagerBean pager;
        private List<Product> items;

        public PagerBean getPager() {
            return pager;
        }

        public void setPager(PagerBean pager) {
            this.pager = pager;
        }

        public List<Product> getItems() {
            return items;
        }

        public void setItems(List<Product> items) {
            this.items = items;
        }
    }

}
