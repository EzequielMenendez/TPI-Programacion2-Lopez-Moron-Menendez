package entities;

import java.time.LocalDateTime;
import java.util.Objects;

public abstract class Base {
    private Long id;
    private boolean eliminado;
    private LocalDateTime createdAt;

    public Base() {
        this.eliminado = false;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object objeto) {
        if (this == objeto) {
            return true;
        }
        if (objeto == null || getClass() != objeto.getClass()) {
            return false;
        }
        Base base = (Base) objeto;
        return id != null && id.equals(base.id);
    }

    @Override
    public int hashCode() {
        return id != null ? Objects.hash(getClass(), id) : System.identityHashCode(this);
    }
}
