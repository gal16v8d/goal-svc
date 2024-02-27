package com.gsdd.goal.persistence.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.ZonedDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Generated;
import lombok.NoArgsConstructor;

@Generated
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "goals")
public class Goal extends AbstractBaseEntity {
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Id
  private Long goalId;
  private boolean life;
  private Integer forYear;
  private boolean ready;
  private ZonedDateTime readyDate;
  private ZonedDateTime estimateReadyDate;
  private String description;
  @ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name = "userId", referencedColumnName = "userId",
      foreignKey = @ForeignKey(name = "Fk_user_goal"))
  private User user;
}
