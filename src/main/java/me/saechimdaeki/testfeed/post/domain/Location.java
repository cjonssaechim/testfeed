package me.saechimdaeki.testfeed.post.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Location {
	private String address;
	private Double latitude;
	private Double longitude;
}
