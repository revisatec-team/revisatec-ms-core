package cr.tec.revisatec.mscore.member.infrastructure.persistence;

import cr.tec.revisatec.mscore.member.application.port.out.GroupMemberRepositoryPort;
import cr.tec.revisatec.mscore.member.domain.GroupMember;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class GroupMemberRepositoryAdapter implements GroupMemberRepositoryPort {

    private final GroupMemberJpaRepository repository;

    public GroupMemberRepositoryAdapter(GroupMemberJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<GroupMember> findByGroupId(Long groupId) {
        return repository.findByGroupIdOrderByNameAsc(groupId).stream()
                .map(GroupMemberRepositoryAdapter::toDomain).toList();
    }

    @Override
    public List<GroupMember> findByEmail(String email) {
        return repository.findByEmail(email).stream().map(GroupMemberRepositoryAdapter::toDomain).toList();
    }

    @Override
    public Optional<GroupMember> findById(Long id) {
        return repository.findById(id).map(GroupMemberRepositoryAdapter::toDomain);
    }

    @Override
    public GroupMember save(GroupMember member) {
        return toDomain(repository.save(new GroupMemberEntity(
                member.id(), member.groupId(), member.name(), member.email(),
                member.githubUser(), member.createdAt())));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public void deleteByGroupId(Long groupId) {
        repository.deleteByGroupId(groupId);
    }

    private static GroupMember toDomain(GroupMemberEntity entity) {
        return new GroupMember(
                entity.getId(), entity.getGroupId(), entity.getName(), entity.getEmail(),
                entity.getGithubUser(), entity.getCreatedAt());
    }
}
