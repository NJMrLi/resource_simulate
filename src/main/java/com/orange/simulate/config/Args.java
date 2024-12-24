package com.orange.simulate.config;

public class Args {

    public String getMemory() {
        return memory;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }

    public String getRates() {
        return rates;
    }

    public void setRates(String rates) {
        this.rates = rates;
    }

    private  String rates;
    private  String memory;

    public Args(String rates, String memory) {
        this.rates = rates;
        this.memory = memory;
    }

}
