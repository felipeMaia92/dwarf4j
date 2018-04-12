package dwarf4j.database;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import dwarf4j.framework.orm.generic.GenericEntity;

@Entity
@Table(name = "dummy")
public class Dummy extends GenericEntity<Integer> {

  @Id
  @GeneratedValue
  @Column(name = "id")
  private Integer id;
  public Integer getId() { return this.id; }
  public void setId(Integer id) { this.id = id; }

  @Column(name = "hue")
  private String hue;
  public String getHue() { return this.hue; }
  public void setHue(String hue) { this.hue = hue; }

}
