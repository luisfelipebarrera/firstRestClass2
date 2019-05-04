package com.multiplication.rest.multiplication.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.multiplication.rest.multiplication.domain.Multiplication;
import com.multiplication.rest.multiplication.domain.MultiplicationResultAttempt;
import com.multiplication.rest.multiplication.domain.User;
import com.multiplication.rest.multiplication.repository.MultiplicationResultAttemptRepository;
import com.multiplication.rest.multiplication.repository.UserRepository;

@Service
class MultiplicationServiceImpl implements MultiplicationService {

	private RandomGeneratorService randomGeneratorService;
	private MultiplicationResultAttemptRepository attemptRepository;
	private UserRepository userRepository;

	@Autowired
	public MultiplicationServiceImpl(final RandomGeneratorService randomGeneratorService,
			final MultiplicationResultAttemptRepository attemptRepository, final UserRepository userRepository) {
		this.randomGeneratorService = randomGeneratorService;
		this.attemptRepository = attemptRepository;
		this.userRepository = userRepository;
	}

	@Override
	public Multiplication createRandomMultiplication() {
		Long id = null;
		int factorA = randomGeneratorService.generateRandomFactor();
		int factorB = randomGeneratorService.generateRandomFactor();
		return new Multiplication(id, factorA, factorB);
	}

	@Override
	public boolean checkAttempt(final MultiplicationResultAttempt resultAttempt) {

		// Check if the user already exists for that alias
		Optional<User> user = userRepository.findByAlias(resultAttempt.getUser().getAlias());

		// Avoids 'hack' attempts
		Assert.isTrue(!resultAttempt.isCorrect(), "You can't send an attempt marked as correct!!");

		// Check if the attempt is correct
		boolean isCorrect = resultAttempt.getResultAttempt() == resultAttempt.getMultiplication().getFactorA()
				* resultAttempt.getMultiplication().getFactorB();

		MultiplicationResultAttempt checkedAttempt = new MultiplicationResultAttempt(
				resultAttempt.getId(),
				user.orElse(resultAttempt.getUser()), resultAttempt.getMultiplication(),
				resultAttempt.getResultAttempt(), isCorrect);

		// Stores the attempt
		attemptRepository.save(checkedAttempt);

		return isCorrect;
	}

	@Override
	public List<MultiplicationResultAttempt> getStatsForUser(String userAlias) {
		return attemptRepository.findTop5ByUserAliasOrderByIdDesc(userAlias);
	}
}
