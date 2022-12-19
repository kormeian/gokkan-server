package com.gokkan.gokkan.domain.expertInfo.domain;

import com.gokkan.gokkan.domain.member.domain.Member;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import jdk.jshell.Snippet;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExpertInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "expert_info_id")
	private Long id;

	@JoinColumn(name = "member_id")
	@OneToOne(fetch = FetchType.LAZY)
	private Member member;

	private String name;
	private String info;

	@Builder
	public ExpertInfo(Member member, String name, String info) {
		this.member = member;
		this.name = name;
		this.info = info;
	}

	@Builder(builderMethodName = "TestOnlyBuilder")
	public ExpertInfo(Long id, Member member, String name, String info) {
		this.id = id;
		this.member = member;
		this.name = name;
		this.info = info;
	}

	public void updateInfo(String info) {
		this.info = info;
	}
}
