package com.vc.model;

public class Trip
{
    private String startDate;

    private HotelInformation hotelInformation;

    private City origin;

    private String endDate;

    private String budgetLimit;

    private String currency;

    private City destination;

    public String getStartDate ()
    {
        return startDate;
    }

    public void setStartDate (String startDate)
    {
        this.startDate = startDate;
    }

    public HotelInformation getHotelInformation ()
    {
        return hotelInformation;
    }

    public void setHotelInformation (HotelInformation hotelInformation)
    {
        this.hotelInformation = hotelInformation;
    }

    public City getOrigin ()
    {
        return origin;
    }

    public void setOrigin (City origin)
    {
        this.origin = origin;
    }

    public String getEndDate ()
    {
        return endDate;
    }

    public void setEndDate (String endDate)
    {
        this.endDate = endDate;
    }

    public String getBudgetLimit ()
    {
        return budgetLimit;
    }

    public void setBudgetLimit (String budgetLimit)
    {
        this.budgetLimit = budgetLimit;
    }

    public String getCurrency ()
    {
        return currency;
    }

    public void setCurrency (String currency)
    {
        this.currency = currency;
    }

    public City getDestination ()
    {
        return destination;
    }

    public void setDestination (City destination)
    {
        this.destination = destination;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [startDate = "+startDate+", hotelInformation = "+hotelInformation+", origin = "+origin+", endDate = "+endDate+", budgetLimit = "+budgetLimit+", currency = "+currency+", destination = "+destination+"]";
    }
}
