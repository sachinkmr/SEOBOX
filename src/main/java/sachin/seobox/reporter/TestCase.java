package sachin.seobox.reporter;

import java.util.Date;

import com.relevantcodes.extentreports.LogStatus;

public class TestCase {
    private LogStatus status;
    private String id;
    private String name;
    private Date time;

  
    public LogStatus getStatus() {
        return this.status;
    }

    public void setStatus(LogStatus status) {
        this.status = status;
    }

    public String getId() {
	return this.id;
    }

    public void setId(String id) {
	this.id = id;
    }

    public String getName() {
	return this.name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public Date getTime() {
        return this.time;
    }

    public void setTime(Date time) {
        this.time = time;
    }



}
