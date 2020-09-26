package com.kaue.model;

import java.io.Serializable;

public interface PersistentEntity<T extends Serializable>
{
    public Long getId();
}