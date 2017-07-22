package com.vc.model;

public class HotelInformation
{
    private String numberOfGuests;

    private String numberOfRooms;

    private String[] ratings;

    public String getNumberOfGuests ()
    {
        return numberOfGuests;
    }

    public void setNumberOfGuests (String numberOfGuests)
    {
        this.numberOfGuests = numberOfGuests;
    }

    public String getNumberOfRooms ()
    {
        return numberOfRooms;
    }

    public void setNumberOfRooms (String numberOfRooms)
    {
        this.numberOfRooms = numberOfRooms;
    }

    public String[] getRatings ()
    {
        return ratings;
    }

    public void setRatings (String[] ratings)
    {
        this.ratings = ratings;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [numberOfGuests = "+numberOfGuests+", numberOfRooms = "+numberOfRooms+", ratings = "+ratings+"]";
    }
}