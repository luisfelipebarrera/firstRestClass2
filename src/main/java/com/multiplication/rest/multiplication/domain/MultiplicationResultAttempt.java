package com.multiplication.rest.multiplication.domain;


import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
@Entity
public class MultiplicationResultAttempt {

	@Id
	@GeneratedValue
	@Column(name="MULTIPLICATIONRESULTATTEMPT_ID")
	private final Long id;
	
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name="USER_ID")
	private final User user;
	
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name="MULTIPLICATION_ID")
    private final Multiplication multiplication;
	
    private final int resultAttempt;
    private final boolean correct;

    // Empty constructor for JSON (de)serialization
    MultiplicationResultAttempt() {
    	id = null;
        user = null;
        multiplication = null;
        resultAttempt = -1;
        correct = false;
    }

}
