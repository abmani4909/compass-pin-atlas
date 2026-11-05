package com.meridian.compasspin.domain.model

enum class Grade(val threshold: Int) {
    S(4250),
    A(3500),
    B(2600),
    C(1600),
    D(0);

    val letter: String get() = name
}
