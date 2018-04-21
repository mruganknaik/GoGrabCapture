package Beans;

public class GenericNotification
{
    private String fuid;
    private String nid;
    private String pid;
    private String status;
    private int type;

    public String getFuid()
    {
        return this.fuid;
    }

    public String getNid()
    {
        return this.nid;
    }

    public String getPid()
    {
        return this.pid;
    }

    public String getStatus()
    {
        return this.status;
    }

    public int getType()
    {
        return this.type;
    }

    public void setFuid(String paramString)
    {
        this.fuid = paramString;
    }

    public void setNid(String paramString)
    {
        this.nid = paramString;
    }

    public void setPid(String paramString)
    {
        this.pid = paramString;
    }

    public void setStatus(String paramString)
    {
        this.status = paramString;
    }

    public void setType(int paramInt)
    {
        this.type = paramInt;
    }
}
