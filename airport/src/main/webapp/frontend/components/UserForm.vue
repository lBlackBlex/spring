<template>
  <section>
    <b-field
      label="Email"
      label-position="on-border"
      :type="input.type"
      :custom-class="input.customClass"
      :message="input.message">
      <b-input required v-model="user.email"/>
    </b-field>
    <b-field
      label="Name"
      label-position="on-border">
      <b-input required v-model="user.name"/>
    </b-field>
    <b-field
      label="Last name"
      label-position="on-border">
      <b-input required v-model="user.last_name"/>
    </b-field>
    <b-field v-if="!showButton" label="Assign role" label-position="on-border">
      <b-autocomplete
        v-model="roleToAdd"
        :open-on-focus="true"
        :data="roleOptions"
        field="name"
        :clearable="true">
      </b-autocomplete>
    </b-field>
    <div class="buttons">
      <b-button
        label="Update User"
        type="is-primary"
        icon-left="pencil"
        @click="update"/>
    </div>
  </section>
</template>

<script>
export default {
  props: {
    user: {
      type: Object,
      required: true
    },
    showButton: {
      type: Boolean,
      required: true
    }
  },
  data() {
    return {
      roleToAdd: '',
      roleOptions: ['USER', 'PILOT', 'EMPLOYEE', 'ADMIN'],
      input:{
          type: '',
          customClass: '',
          message: ''
        }
    }
  },
  created() {
    this.setUserRole()
  },
  methods: {
    goBack(){
      this.$router.back()
    },
    setUserRole(){
      this.roleToAdd = this.user.role
    },
    errorNotification(){
      this.$buefy.notification.open({
        duration: 2500,
        message: `This user has <b>planes</b> assigned, first delete them`,
        type: 'is-danger',
        hasIcon: true
      })
    },
    update() {
      if(this.user.email.length === 0){
        this.input.email.type = 'is-danger'
        this.input.email.message = 'This field cannot be empty'
        this.input.email.customClass = 'has-text-danger'
        return
      }
      if(this.user.name.length === 0) return
      if(this.user.last_name.length === 0) return
      if(this.user.role !== this.roleToAdd)
        if(this.user.planes.length !== 0) {
          this.errorNotification()
          return
        }

      this.$axios.$put(
        `http://localhost:8080/api/v1/users/${this.$route.params.id}`,
        {},
        {
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `${this.$store.getters.getToken}`
          },
          params: {
            'email': `${this.user.email}`,
            'name': `${this.user.name}`,
            'last_name': `${this.user.last_name}`,
            'role': `${this.roleToAdd}`
          }
        }).then(() => {
        this.$buefy.toast.open({
          message: 'User updated!',
          type: 'is-success'
        })
        this.input.type = ''
        this.input.customClass = ''
        this.input.message = ''
      }).catch(() => {
        this.input.type = 'is-danger'
        this.input.customClass = 'has-text-danger'
        this.input.message = 'Email already in use'
        this.$buefy.notification.open({
          duration: 3000,
          message: `Email: <b>${this.user.email}</b> is already in use`,
          type: 'is-warning',
          hasIcon: true
        })
      })
      console.log('update ' + this.user.id)
    },
  }
}
</script>
