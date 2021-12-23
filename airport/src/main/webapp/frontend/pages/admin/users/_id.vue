<template>
  <section class="section">
    <div class="columns is-mobile">
      <section class="column">
        <div class="buttons">
          <b-button
            label="Go Back"
            type="is-primary"
            icon-left="arrow-left"
            @click="goBack"/>
        </div>
        <b-taglist attached>
          <b-tag type="is-dark" size="is-large">
            <b-icon
              icon="pound"
              size="is-small">
            </b-icon>
          </b-tag>
          <b-tag type="is-info" size="is-large">{{ user.id }}</b-tag>
        </b-taglist>
        <UserForm
          :user="user"
          :showButton="false"/>
        <br>
        <b-message type="is-info" has-icon>
          Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce id fermentum quam. Proin sagittis, nibh id
          hendrerit imperdiet, elit sapien laoreet elit
        </b-message>
      </section>
    </div>
  </section>
</template>

<script>
import UserForm from "@/components/UserForm";

export default {
  middleware: ['check-auth', 'auth'],
  components: [
    UserForm
  ],
  created() {
    this.getUser()
  },
  data() {
    return {
      user: null,
    }
  },
  methods: {
    goBack() {
      this.$router.back()
    },
    getUser() {
      const users = this.$store.getters.users
      this.user = users.find(user => user.id === this.$route.params.id)
    }
  }
}
</script>
