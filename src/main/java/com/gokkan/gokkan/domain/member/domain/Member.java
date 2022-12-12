package com.gokkan.gokkan.domain.member.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gokkan.gokkan.global.security.oauth.entity.ProviderType;
import com.gokkan.gokkan.global.security.oauth.entity.RoleType;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

	@JsonIgnore
	@Column(name = "PASSWORD", length = 128)
	@NotNull
	@Size(max = 128)
	private String password;

	@Column(name = "EMAIL", length = 512, unique = true)
	@NotNull
	@Size(max = 512)
	private String email;

	@Column(name = "PROFILE_IMAGE_URL", length = 512)
	@NotNull
	@Size(max = 512)
	private String profileImageUrl;

	@Column(name = "PROVIDER_TYPE", length = 20)
	@Enumerated(EnumType.STRING)
	@NotNull
	private ProviderType providerType;

	@Column(name = "ROLE_TYPE", length = 20)
	@Enumerated(EnumType.STRING)
	@NotNull
	private RoleType roleType;

	@Column(name = "CREATED_AT")
	@NotNull
	private LocalDateTime createdAt;

	@Column(name = "MODIFIED_AT")
	@NotNull
	private LocalDateTime modifiedAt;

	public Member(
		@NotNull @Size(max = 64) String userId,
		@NotNull @Size(max = 100) String name,
		@NotNull @Size(max = 512) String email,
		@NotNull @Size(max = 512) String profileImageUrl,
		@NotNull ProviderType providerType,
		@NotNull RoleType roleType,
		@NotNull LocalDateTime createdAt,
		@NotNull LocalDateTime modifiedAt
	) {
		this.userId = userId;
		this.name = name;
		this.password = "NO_PASS";
		this.email = email != null ? email : "NO_EMAIL";
		this.profileImageUrl = profileImageUrl != null ? profileImageUrl : "";
		this.providerType = providerType;
		this.roleType = roleType;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;
	}
}

