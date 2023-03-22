package edu.northeastern.cs5500.starterbot.model;

import org.bson.types.ObjectId;

/** This model contains minimum amount of what we can store in the MongoBD */
public interface Model {
    ObjectId getId(); // Everything in MongoDB has to have an ID

    void setId(ObjectId id); // The id is randomly generated
}
