/**
 * Copyright 2020 lambdaprime
 * 
 * Email: id.blackmesa@gmail.com 
 * Website: https://github.com/lambdaprime
 * 
 */
package id.fireguard.tests;

import java.io.Serializable;
import java.util.Optional;

import id.xfunction.ObjectStore;

public class ObjectStoreMock<T extends Serializable> extends ObjectStore<T> {

    public ObjectStoreMock() {
        super(null);
    }

    @Override
    public void save(T obj) {

    }

    @Override
    public Optional<T> load() {
        return Optional.empty();
    }

}