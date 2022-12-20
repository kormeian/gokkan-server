package com.gokkan.gokkan.domain.member.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gokkan.gokkan.global.security.oauth.entity.ProviderType;
import com.gokkan.gokkan.global.security.oauth.entity.Role;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member {

	@JsonIgnore
	@Id
	@Column(name = "member_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "USER_ID", length = 64, unique = true)
	@NotNull
	@Size(max = 64)
	private String userId;

	@Column(name = "name", length = 100)
	@NotNull
	@Size(max = 100)
	private String name;

	@Column(name = "EMAIL", length = 512, unique = true)
	@NotNull
	@Size(max = 512)
	private String email;

	@Column(name = "PROFILE_IMAGE_URL", length = 512)
	@NotNull
	@Size(max = 512)
	private String profileImageUrl;

	private String phoneNumber;
	private String cardNumber;
	private String address;

	@Column(name = "PROVIDER_TYPE", length = 20)
	@Enumerated(EnumType.STRING)
	@NotNull
	private ProviderType providerType;

	@Column(name = "ROLE", length = 20)
	@Enumerated(EnumType.STRING)
	@NotNull
	private Role role;

	@Builder
	public Member(
		@NotNull @Size(max = 64) String userId,
		@NotNull @Size(max = 100) String name,
		@NotNull @Size(max = 512) String email,
		@NotNull @Size(max = 512) String profileImageUrl,
		@NotNull ProviderType providerType,
		@NotNull Role role) {

		this.userId = userId;
		this.name = name;
		this.email = email != null ? email : "NO_EMAIL";
		this.profileImageUrl = profileImageUrl != null ? profileImageUrl : "";
		this.providerType = providerType;
		this.role = role;
	}
}

